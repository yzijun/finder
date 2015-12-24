'use strict';

angular.module('finderApp').controller('ForbiddenWordDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ForbiddenWord',
        function($scope, $stateParams, $uibModalInstance, entity, ForbiddenWord) {

        $scope.forbiddenWord = entity;
        $scope.load = function(id) {
            ForbiddenWord.get({id : id}, function(result) {
                $scope.forbiddenWord = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:forbiddenWordUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.forbiddenWord.id != null) {
                ForbiddenWord.update($scope.forbiddenWord, onSaveSuccess, onSaveError);
            } else {
                ForbiddenWord.save($scope.forbiddenWord, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
