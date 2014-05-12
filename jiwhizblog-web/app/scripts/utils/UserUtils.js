function processUser(user) {
    user.isAdmin = function() {
        return _.contains(user.roles, 'ROLE_ADMIN');
    };
    
    user.isAuthor = function() {
        return _.contains(user.roles, 'ROLE_AUTHOR');
    };

}