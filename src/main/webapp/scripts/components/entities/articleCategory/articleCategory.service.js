'use strict';

angular.module('finderApp')
    .factory('ArticleCategory', function ($resource, DateUtils) {
        return $resource('api/articleCategorys/:id', {}, {
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
