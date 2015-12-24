'use strict';

describe('ArticleReply Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockArticleReply, MockArticle, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockArticleReply = jasmine.createSpy('MockArticleReply');
        MockArticle = jasmine.createSpy('MockArticle');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ArticleReply': MockArticleReply,
            'Article': MockArticle,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("ArticleReplyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'finderApp:articleReplyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
