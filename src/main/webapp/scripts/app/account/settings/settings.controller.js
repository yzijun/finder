'use strict';

angular.module('finderApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate) {
    	// 增加用户上传头像
        $scope.byteSize = byteSize;
        $scope.setPicture = setPicture;
    	
        $scope.success = null;
        $scope.error = null;
        //验证出错后的显示信息
        $scope.errorEmailExists = null;
        $scope.uploadPictureLarge = null;
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.errorEmailExists = null;
                $scope.uploadPictureLarge = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function(httpResponse) {
                $scope.success = null;
                //根据后台传回来的值判断出错类型
                var errorHeader = httpResponse.headers('X-finderApp-error');
                if (errorHeader === 'error.emailexists') {
                    $scope.errorEmailExists = 'ERROR';
                } else if (errorHeader === 'error.uploadpicturelarge') {
                    $scope.uploadPictureLarge = 'ERROR';
                } else {
                    $scope.error = 'ERROR';
                }
            });
        };
        
        function byteSize(base64String) {
            if (!angular.isString(base64String)) {
              return '';
            }
            function endsWith(suffix, str) {
              return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            function paddingSize(base64String) {
              if (endsWith('==', base64String)) {
                return 2;
              }
              if (endsWith('=', base64String)) {
                return 1;
              }
              return 0;
            }

            function size(base64String) {
              return base64String.length / 4 * 3 - paddingSize(base64String);
            }

            function formatAsBytes(size) {
              return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
          }

          function setPicture($file, settingsAccount) {
            if ($file && $file.$error == 'pattern') {
              return;
            }
            if ($file) {
              var fileReader = new FileReader();
              fileReader.readAsDataURL($file);
              fileReader.onload = function (e) {
                var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                $scope.$apply(function () {
                  settingsAccount.picture = base64Data;
                  settingsAccount.pictureContentType = $file.type;
                });
            }
          }
        }
        
    });
