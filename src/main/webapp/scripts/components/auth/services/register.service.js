'use strict';

angular.module('finderApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


