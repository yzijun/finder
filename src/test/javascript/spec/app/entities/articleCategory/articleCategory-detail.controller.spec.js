'use strict';

describe('ArticleCategory Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockArticleCategory, MockArticle;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockArticleCategory = jasmine.createSpy('MockArticleCategory');
        MockArticle = jasmine.createSpy('MockArticle');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ArticleCategory': MockArticleCategory,
            'Article': MockArticle
        };
        createController = function() {
            $injector.get('$controller')("ArticleCategoryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'finderApp:articleCategoryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
