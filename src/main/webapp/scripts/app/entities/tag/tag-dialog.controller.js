'use strict';

angular.module('finderApp').controller('TagDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tag', 'Article',
        function($scope, $stateParams, $uibModalInstance, entity, Tag, Article) {

        $scope.tag = entity;
        $scope.articles = Article.query();
        $scope.load = function(id) {
            Tag.get({id : id}, function(result) {
                $scope.tag = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:tagUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tag.id != null) {
                Tag.update($scope.tag, onSaveSuccess, onSaveError);
            } else {
                Tag.save($scope.tag, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
