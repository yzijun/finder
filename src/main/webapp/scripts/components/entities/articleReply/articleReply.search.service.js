'use strict';

angular.module('finderApp')
    .factory('ArticleReplySearch', function ($resource) {
        return $resource('api/_search/articleReplys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
