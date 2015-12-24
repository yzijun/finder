'use strict';

describe('Article Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockArticle, MockUser, MockArticleCategory, MockTag;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockArticle = jasmine.createSpy('MockArticle');
        MockUser = jasmine.createSpy('MockUser');
        MockArticleCategory = jasmine.createSpy('MockArticleCategory');
        MockTag = jasmine.createSpy('MockTag');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Article': MockArticle,
            'User': MockUser,
            'ArticleCategory': MockArticleCategory,
            'Tag': MockTag
        };
        createController = function() {
            $injector.get('$controller')("ArticleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'finderApp:articleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
