package com.tienda.service;

import com.tienda.dao.UsuarioDao;
import com.tienda.dao.CreditoDao;
import com.tienda.domain.Usuario;
import com.tienda.domain.Credito;
import com.tienda.domain.Rol;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Se busca el usuario en la tabla de usuarios
        Usuario usuario = usuarioDao.findByUsername(username);
        
        //Usuario no existe en BD
        if (usuario == null){
            throw new UsernameNotFoundException(username);
        }
        
        //Se cargan los roles del usuario en un arreglo especial de tipo GrantedAuthority
        var roles = new ArrayList<GrantedAuthority>();
        
        //Se recorre el ArrayList del objeto usuario y se transforma en GrantedAuthority cada rol
        for (Rol rol:usuario.getRoles()){
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }
        
        //Retorna un UserDetails con la info recuperada
        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }
}
