package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.exception.RegraDeNegociosException;
import br.projeto_integrador.aplicativo.backend.model.dto.GoogleUserInfoDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioCadastroDTO;
import br.projeto_integrador.aplicativo.backend.model.dto.UsuarioDTO;
import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.model.enums.StatusUsuario;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioDTO criarUsuario (UsuarioCadastroDTO dto){

        if(usuarioRepository.existsByEmail(dto.email())){
            throw new RegraDeNegociosException("Email já cadastrado");
        }

        if(usuarioRepository.existsByCpf(dto.cpf())){
            throw new RegraDeNegociosException("CPF já cadastrado");        }


        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setTelefone(dto.telefone());
        usuario.setEmail(dto.email());
        usuario.setStatus(StatusUsuario.ATIVO);

        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioDTO(
                usuarioSalvo.getIdUsuario(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail()
        );
    }

    public List<UsuarioDTO> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> listaUsuarios = new ArrayList<>();

        for(Usuario usuario : usuarios){

            if(usuario.getStatus() == StatusUsuario.INATIVO){
                continue;
            }

            listaUsuarios.add(new UsuarioDTO(
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    usuario.getEmail()));
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

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
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

}
