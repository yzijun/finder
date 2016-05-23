'use strict';

angular.module('finderApp')
    .controller('RegisterController', function ($scope, $translate, $timeout, Auth, CommonTools) {
    	// 默认回到页面的顶部
    	CommonTools.scrollTo();
    	
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {};
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.errorEmailExists = null;

                Auth.createAccount($scope.registerAccount).then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        $scope.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        $scope.errorEmailExists = 'ERROR';
                    } else {
                        $scope.error = 'ERROR';
                    }
                });
            }
        };
        // 密码和确认密码不一致时设定error样式和注册按钮不可用
        // 不用了$scope.register有判断
       /* $scope.checkConfirmPassword = function() {
        	if ($scope.form.confirmPassword.$dirty 
        			&& ($scope.registerAccount.password 
        			!= $scope.confirmPassword)) {
        		$scope.form.confirmPassword.$invalid = true;
        		$scope.form.$invalid = true;
        	}
        	if ($scope.form.confirmPassword.$dirty 
        			&& ($scope.registerAccount.password 
        			== $scope.confirmPassword)) {
        		$scope.form.confirmPassword.$invalid = false;
        		$scope.form.$invalid = false;
        	}
        };*/
        
    });
