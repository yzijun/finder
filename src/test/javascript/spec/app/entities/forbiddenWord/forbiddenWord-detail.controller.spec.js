'use strict';

describe('ForbiddenWord Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockForbiddenWord;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockForbiddenWord = jasmine.createSpy('MockForbiddenWord');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'ForbiddenWord': MockForbiddenWord
        };
        createController = function() {
            $injector.get('$controller')("ForbiddenWordDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'finderApp:forbiddenWordUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
