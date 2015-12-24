'use strict';

angular.module('finderApp').controller('ArticleReplyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArticleReply', 'Article', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, ArticleReply, Article, User) {

        $scope.articleReply = entity;
        $scope.articles = Article.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            ArticleReply.get({id : id}, function(result) {
                $scope.articleReply = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:articleReplyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.articleReply.id != null) {
                ArticleReply.update($scope.articleReply, onSaveSuccess, onSaveError);
            } else {
                ArticleReply.save($scope.articleReply, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
