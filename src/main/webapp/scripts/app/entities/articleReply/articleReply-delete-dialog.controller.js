'use strict';

angular.module('finderApp')
	.controller('ArticleReplyDeleteController', function($scope, $uibModalInstance, entity, ArticleReply) {

        $scope.articleReply = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ArticleReply.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
