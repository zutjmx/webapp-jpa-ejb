package org.zutjmx.apiservlet.webapp.headers.services;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.zutjmx.apiservlet.webapp.headers.configs.Service;
import org.zutjmx.apiservlet.webapp.headers.models.entities.Usuario;
import org.zutjmx.apiservlet.webapp.headers.repositories.RepositoryJpa;
import org.zutjmx.apiservlet.webapp.headers.repositories.UsuarioRepository;

import java.util.Optional;

@Service
@Stateless
public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository usuarioRepository;

    @Inject
    public UsuarioServiceImpl(@RepositoryJpa UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> login(String username, String password) {
        try {
            return Optional.ofNullable(usuarioRepository.porUsername(username))
                    .filter(usuario -> usuario.getPassword().equals(password));
        } catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(),e.getCause());
        }
    }
}
