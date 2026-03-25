package br.projeto_integrador.aplicativo.backend.services;

import br.projeto_integrador.aplicativo.backend.model.entity.Usuario;
import br.projeto_integrador.aplicativo.backend.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = this.usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário ou senha incorretos");
        } else{

            // Cria o User do Spring Security com email, senha e autoridade
            //cria objeto User do spring security
            //passa o nome de usuario
            //passa a senha
            //passa o role/autoridade
            //User implementa UserDetails, que é usado pelo Spring Security para autenticação e autorização

            if(usuario.getSenha() == null){
                usuario.setSenha("");
            }

            UserDetails user = User.withUsername(usuario.getEmail()).password(usuario.getSenha())
                    .roles("USER")
                    .build();

            return user;
        }

    }
}
