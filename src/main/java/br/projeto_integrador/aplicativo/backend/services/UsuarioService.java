package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.CodigoRecuperacao;
import br.projeto_integrador.aplicativo.backend.model.dto.GoogleUserInfoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final Cloudinary cloudinary;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, Cloudinary cloudinary, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.cloudinary = cloudinary;
        this.emailService = emailService;
    }

    @Transactional
    public UsuarioDTO criarUsuario (UsuarioCadastroDTO dto){

        if(usuarioRepository.existsByEmail(dto.email())){
            throw new RegraDeNegociosException("Email já cadastrado");
        }

        if(usuarioRepository.existsByCpf(dto.cpf())){
            throw new RegraDeNegociosException("CPF já cadastrado");
        }


        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setTelefone(dto.telefone());
        usuario.setEmail(dto.email());
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setSaldo(BigDecimal.valueOf(0.0));

        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }

    public List<UsuarioCadastroDTO> listarUsuarios() {

        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioCadastroDTO> listaUsuarios = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            if (usuario.getStatus() == StatusUsuario.INATIVO) {
                continue;
            }

            listaUsuarios.add(new UsuarioCadastroDTO(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    usuario.getCpf(),
                    usuario.getTelefone(),
                    usuario.getEmail(),
                    usuario.getFotoUrl(),
                    null
            ));
        }

        return listaUsuarios;
    }


    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegociosException("Usuário não encontrado"));
    }



    @Transactional
    public UsuarioDTO  atualizarUsuario(Long id, UsuarioCadastroDTO dto) {

        Usuario usuario = null;
        try {
            usuario = buscarPorId(id);
        } catch (Exception e) {
            throw new RegraDeNegociosException("Usuário não encontrado");
        }

        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }

        if (dto.telefone() != null) {
            usuario.setTelefone(dto.telefone());
        }


        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));
        }

        Usuario atualizado = usuarioRepository.save(usuario);

        return new UsuarioDTO(
                atualizado.getIdUsuario(),
                atualizado.getNome(),
                atualizado.getEmail()
        );

    }

    @Transactional
    public String atualizarFoto(Long idUsuario, MultipartFile foto) {

        if (foto == null || foto.isEmpty()) {
            throw new RegraDeNegociosException("Imagem não enviada ou inválida");
        }

        String contentType = foto.getContentType();

        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new RegraDeNegociosException("Apenas JPG ou PNG são permitidos");
        }

        long tamanhoMaximo = 2 * 1024 * 1024;

        if (foto.getSize() > tamanhoMaximo) {
            throw new RegraDeNegociosException("Imagem muito grande (máx 2MB)");
        }


        try {
            Usuario usuario = buscarPorId(idUsuario);

            Map resultado = cloudinary.uploader().upload(
                    foto.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String url = resultado.get("secure_url").toString();

            usuario.setFotoUrl(url);
            usuarioRepository.save(usuario);

            return url;

        } catch (Exception e) {
            throw new RegraDeNegociosException("Erro ao atualizar foto: " + e.getMessage());
        }
    }

    public void deletarUsuario(Long idUsuario) {

        Usuario usuario = null;
        try {
            usuario = buscarPorId(idUsuario);
        } catch (Exception e) {
            throw new RegraDeNegociosException("Usuário não encontrado");
        }

        usuario.setStatus(StatusUsuario.INATIVO);

        usuarioRepository.save(usuario);


    }



    public Usuario usuarioGoogle(GoogleUserInfoDTO dto){

        Usuario usuario = usuarioRepository.findByEmail(dto.email());

        if(usuario == null){
            usuario = new Usuario();
            usuario.setEmail(dto.email());
            usuario.setNome(dto.nome());
            usuario.setStatus(StatusUsuario.ATIVO);
            usuario.setCadastroCompleto(false);


            usuarioRepository.save(usuario);
        }

        return usuario;
    }


    private Map<String, CodigoRecuperacao> codigos = new HashMap<>();

    public void enviarCodigoRecuperacao(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RegraDeNegociosException("Usuário não encontrado");
        }

        String codigo = String.format("%06d", new Random().nextInt(1000000));

        CodigoRecuperacao codigoRecuperacao = new CodigoRecuperacao();
        codigoRecuperacao.setCodigo(codigo);
        codigoRecuperacao.setValidade(LocalDateTime.now().plusMinutes(10));

        codigos.put(email, codigoRecuperacao);

        emailService.enviar(email,
                "Recuperação de senha",
                "Seu código de recuperação é: " + codigo + "\n\nEle expira em 10 minutos.");

        System.out.println("Código enviado: " + codigo);
    }

    public void redefinirSenha(String email, String codigo, String novaSenha) {

        CodigoRecuperacao codigoRecuperacao = codigos.get(email);

        if(codigoRecuperacao == null){
            throw new RegraDeNegociosException("Código não encontrado");
        }

        if (!codigoRecuperacao.getCodigo().equals(codigo)) {
            throw new RegraDeNegociosException("Código inválido");
        }


        if (codigoRecuperacao.getValidade().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegociosException("Código expirado");
        }

        Usuario usuario = usuarioRepository.findByEmail(email);
        usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
        usuarioRepository.save(usuario);

        codigos.remove(email);


    }
}
