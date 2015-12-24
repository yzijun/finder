'use strict';

angular.module('finderApp').controller('ArticleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Article', 'User', 'ArticleCategory', 'Tag',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Article, User, ArticleCategory, Tag) {

        $scope.article = entity;
        $scope.users = User.query();
        $scope.articlecategorys = ArticleCategory.query();
        $scope.tags = Tag.query();
        $scope.load = function(id) {
            Article.get({id : id}, function(result) {
                $scope.article = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:articleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.article.id != null) {
                Article.update($scope.article, onSaveSuccess, onSaveError);
            } else {
                Article.save($scope.article, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.setFirstImg = function ($file, article) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        article.firstImg = base64Data;
                        article.firstImgContentType = $file.type;
                    });
                };
            }
        };
}]);
