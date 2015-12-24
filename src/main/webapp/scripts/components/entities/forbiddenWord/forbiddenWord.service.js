'use strict';

angular.module('finderApp')
    .factory('ForbiddenWord', function ($resource, DateUtils) {
        return $resource('api/forbiddenWords/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
