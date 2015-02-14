'use strict';

describe('Controller: public/BlogListController', function() {
    var $rootScope, $scope;
    var $controller, service;
   
    var mockWebsite = {
        $get: function(rel) {}
    };
        
    var mockResource = {
        page: {
            size: 10,
            totalElements: 100,
            totalPages: 4,
            number: 0
        },
        $get: function(rel) {}
    };

    var mockBlogPostList = [
        {
            title: 'Test Blog',
            content: '<p>This is first paragraph.</p> Other parts...',
            $get: function(rel) {}
        },
        {
            title: 'Another Blog',
            content: '<p>I came second.</p>',
            $get: function(rel) {}
        }
    ];

    var mockAuthor = {
        displayName: 'author'
    };

    beforeEach(module('jiwhizWeb'));

    beforeEach(inject(function(_$rootScope_, _$controller_, _$q_, _WebsiteService_) {
        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $controller = _$controller_;
        service = _WebsiteService_;

        var websiteDeferred = _$q_.defer();
        websiteDeferred.resolve(mockWebsite);
        spyOn(service, 'load').and.returnValue(websiteDeferred.promise);
        
        var blogsDeferred = _$q_.defer();
        blogsDeferred.resolve(mockResource);
        spyOn(mockWebsite, '$get').and.returnValue(blogsDeferred.promise);

        var blogListDeferred = _$q_.defer();
        blogListDeferred.resolve(mockBlogPostList);
        spyOn(mockResource, '$get').and.returnValue(blogListDeferred.promise);

        var authorDeferred = _$q_.defer();
        authorDeferred.resolve(mockAuthor);
        spyOn(mockBlogPostList[0], '$get').and.returnValue(authorDeferred.promise);
        spyOn(mockBlogPostList[1], '$get').and.returnValue(authorDeferred.promise);

        $controller('BlogListController', 
            {'$rootScope' : $rootScope, '$scope': $scope, 'WebsiteService': service});
        $rootScope.$apply(); // promises are resolved/dispatched only on next $digest cycle
    }));

    it('should make Blog menu item active.', function() {
        expect($rootScope.activeMenu.blog == 'active');
    });

    it('should have selectBlogPage() function.', function() {
        expect($scope.pageChanged).toBeDefined();
    });

    describe('BlogListController setup(pageNumber) function', function() {
        
        it('should have currentPage set to 1.', function() {
            expect($scope.page.currentPage).toBe(1);
        });

        it('should have two blogs and first one is Test Blog.', function() {
            expect($scope.blogs.length).toEqual(2);
            expect($scope.blogs[0].title).toEqual('Test Blog');
        });
        
        it('should have first blog with title "Test Blog" and author "author"', function() {
            expect($scope.blogs[0].title).toEqual('Test Blog');
            expect($scope.blogs[0].author.displayName).toEqual('author');
        });
        
        it('should have second blog with title "Another Blog" and author "author"', function() {
            expect($scope.blogs[1].title).toEqual('Another Blog');
            expect($scope.blogs[1].author.displayName).toEqual('author');
        });

    });
});