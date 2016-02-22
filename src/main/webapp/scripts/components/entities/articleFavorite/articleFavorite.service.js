'use strict';

angular.module('finderApp')
    .factory('ArticleFavorite', function ($resource, DateUtils) {
        return $resource('api/articleFavorites/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
