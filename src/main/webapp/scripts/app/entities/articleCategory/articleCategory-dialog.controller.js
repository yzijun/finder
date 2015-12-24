'use strict';

angular.module('finderApp').controller('ArticleCategoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArticleCategory', 'Article',
        function($scope, $stateParams, $uibModalInstance, entity, ArticleCategory, Article) {

        $scope.articleCategory = entity;
        $scope.articles = Article.query();
        $scope.load = function(id) {
            ArticleCategory.get({id : id}, function(result) {
                $scope.articleCategory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:articleCategoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.articleCategory.id != null) {
                ArticleCategory.update($scope.articleCategory, onSaveSuccess, onSaveError);
            } else {
                ArticleCategory.save($scope.articleCategory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
