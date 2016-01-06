'use strict';

angular.module('finderApp').controller('ArticleDialogController',
    ['$scope', '$state','DataUtils', 'entity', 'Article', 'User', 'ArticleCategory', 'Tag',
        function($scope, $state, DataUtils, entity, Article, User, ArticleCategory, Tag) {

        $scope.article = entity;
        //注释不用,会查询全部的用户
//        $scope.users = User.query();
        $scope.articlecategorys = ArticleCategory.query();
//        $scope.tags = Tag.query();
        $scope.load = function(id) {
            Article.get({id : id}, function(result) {
                $scope.article = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('finderApp:articleUpdate', result);
            $scope.isSaving = false;
            $state.go('article', null, { reload: true });
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
        	//设置百度编辑器的内容
        	//$scope.article.content = UM.getEditor('myEditor').getContent();
        	//设置bootstrap-wysiwyg文本编辑器的内容
        	//cleanHtml(true)和html()同时可以上传图片和文字，图片是编码后的字符串
        	$scope.article.content = $('#editor').html();
            $scope.isSaving = true;
            if ($scope.article.id != null) {
                Article.update($scope.article, onSaveSuccess, onSaveError);
            } else {
                Article.save($scope.article, onSaveSuccess, onSaveError);
            }
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.setFirstImg = function ($file, article) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
               /* var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        article.firstImg = base64Data;
                        article.firstImgContentType = $file.type;
                    });
                };*/
            	//localResizeIMG客户端浏览器缩放图片 宽度：800   高度自适应
            	lrz($file, {width: 800})
                .then(function (rst) {
                    // 把处理的好的图片给用户看看呗
                	var base64Data = rst.base64.substr(rst.base64.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        article.firstImg = base64Data;
                        article.firstImgContentType = $file.type;
                    });

                    return rst;
                }).catch(function (err) {
                    // 万一出错了，这里可以捕捉到错误信息
                    // 而且以上的then都不会执行
                    alert(err);
                });
            }
        };
        //删除选择的上传图片时显示错误提示
        $scope.isupload = false;
        $scope.setIsupload = function() {
        	$scope.isupload = true;
        }
}]);
