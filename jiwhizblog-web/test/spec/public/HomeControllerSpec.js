'use strict';

describe('Controller: public/HomeController', function() {
    var $rootScope, $scope;
    var $controller, service;
    var websiteDeferred, blogDeferred, authorDeferred;

    var mockWebsite = {
        $get: function(rel) {}
    };
    
    var mockBlogPost = {
        title: 'Test Blog',
        content: '<p>This is first paragraph.</p> Other parts...',
        $get: function(rel) {}
    };

    var mockAuthor = {
        displayName: 'author'
    };

    beforeEach(module('jiwhizWeb'));
    
    beforeEach(inject(function(_$rootScope_, _$controller_, _$q_, _$timeout_, _WebsiteService_){
        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $controller = _$controller_;
        service = _WebsiteService_;

        websiteDeferred = _$q_.defer();
        websiteDeferred.resolve(mockWebsite);
        spyOn(service, 'load').andReturn(websiteDeferred.promise);
        
        blogDeferred = _$q_.defer();
        spyOn(mockWebsite, '$get').andReturn(blogDeferred.promise);

        authorDeferred = _$q_.defer();
        spyOn(mockBlogPost, '$get').andReturn(authorDeferred.promise);

        $controller('HomeController', 
            {
                '$rootScope' : $rootScope, 
                '$scope': $scope,
                '$timeout': _$timeout_,
                'WebsiteService': service
            });
    }));

    it('should have correct UI setup, such as active home menu, show slider, hide title.', function() {
        expect($rootScope.activeMenu.home == 'active');
        expect($rootScope.showTitle == false);
    });
    
    it('should not have blog if latest blog returned is null.', function() {
        blogDeferred.resolve(null);
        $rootScope.$apply();
        expect($scope.hasBlog).toBe(false);
        expect($scope.blog).toBeUndefined();
    });

    it('should have blog if latest blog returned.', function() {
        blogDeferred.resolve(mockBlogPost);
        authorDeferred.resolve(mockAuthor);
        $rootScope.$apply();
        expect($scope.hasBlog).toBe(true);
        expect($scope.blog).not.toBeNull();
        expect($scope.blog.title).toEqual('Test Blog');
    });



});
