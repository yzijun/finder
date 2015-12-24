'use strict';

angular.module('finderApp')
	.controller('ArticleCategoryDeleteController', function($scope, $uibModalInstance, entity, ArticleCategory) {

        $scope.articleCategory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ArticleCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
