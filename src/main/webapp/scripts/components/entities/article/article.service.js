'use strict';

angular.module('finderApp')
    .factory('Article', function ($resource, DateUtils) {
        return $resource('api/articles/:id', {}, {
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
