'use strict';

angular.module('finderApp')
    .factory('ArticleCategorySearch', function ($resource) {
        return $resource('api/_search/articleCategorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
