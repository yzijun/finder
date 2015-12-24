'use strict';

angular.module('finderApp')
    .factory('TagSearch', function ($resource) {
        return $resource('api/_search/tags/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
