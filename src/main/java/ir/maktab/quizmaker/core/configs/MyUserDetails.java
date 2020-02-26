package ir.maktab.quizmaker.core.configs;

import ir.maktab.quizmaker.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;
    private List<GrantedAuthority> authorityList;

    public MyUserDetails(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.enabled = account.isEnabled();
        this.authorityList=account.getRoleList().stream()
                .map(role ->new SimpleGrantedAuthority(role.getRoleName().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
