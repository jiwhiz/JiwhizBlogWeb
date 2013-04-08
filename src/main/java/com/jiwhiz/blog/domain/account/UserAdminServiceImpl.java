package com.jiwhiz.blog.domain.account;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionData;

import com.jiwhiz.blog.domain.system.CounterService;

/**
 * Implementation for UserAdminService.
 * 
 * @author Yuan Ji
 *
 */
public class UserAdminServiceImpl implements UserAdminService {
    public static final String USER_ID_PREFIX = "user";
    
    private final UserAccountRepository accountRepository;
    private final CounterService counterService;

    @Inject
    public UserAdminServiceImpl(UserAccountRepository accountRepository, CounterService counterService) {
        this.accountRepository = accountRepository;
        this.counterService = counterService;
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.account.UserAdminService#createUserAccount(org.springframework.social.connect.ConnectionData)
     */
    @Override
    public UserAccount createUserAccount(ConnectionData data) {
        UserAccount account = new UserAccount();
        account.setUserId(USER_ID_PREFIX + this.counterService.getNextUserIdSequence());
        account.setDisplayName(data.getDisplayName());
        account.setImageUrl(data.getImageUrl());
        account.setRoles(new UserRoleType[] { UserRoleType.ROLE_USER });
        this.accountRepository.save(account);

        return account;
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.account.UserAdminService#setAuthor(java.lang.String, boolean)
     */
    @Override
    public void setAuthor(String userId, boolean isAuthor) throws UsernameNotFoundException{
        UserAccount account = loadUserByUserId(userId);
            Set<UserRoleType> roleSet = new HashSet<UserRoleType>();
            for (UserRoleType role : account.getRoles()) {
                roleSet.add(role);
            }
            if (isAuthor) {
                roleSet.add(UserRoleType.ROLE_AUTHOR);
            } else {
                roleSet.remove(UserRoleType.ROLE_AUTHOR);
            }
            account.setRoles(roleSet.toArray(new UserRoleType[roleSet.size()]));
            this.accountRepository.save(account);
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.social.security.SocialUserDetailsService#loadUserByUserId(java.lang.String)
     */
    @Override
    public UserAccount loadUserByUserId(String userId) throws UsernameNotFoundException {
        UserAccount account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new UsernameNotFoundException("Cannot find user by userId " + userId);
        }
        return account;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUserId(username);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.social.UserIdSource#getUserId()
     */
    @Override
    public String getUserId() {
        return AccountUtils.getLoginUserId();
    }

    /*
     * (non-Javadoc)
     * @see com.jiwhiz.blog.domain.account.UserAdminService#getCurrentUser()
     */
    @Override
    public UserAccount getCurrentUser() {
        return accountRepository.findByUserId(getUserId());
    }
}
