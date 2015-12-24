'use strict';

angular.module('finderApp')
    .factory('ForbiddenWordSearch', function ($resource) {
        return $resource('api/_search/forbiddenWords/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
