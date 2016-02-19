'use strict';

angular.module('finderApp')
    .factory('Article', function ($resource, DateUtils) {
        return $resource('api/articles/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                	// 可能有文章id不存在或是该文章不允许发布
                	if (data != "") {
                		data = angular.fromJson(data);
                		data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                		return data;
                	}
                }
            },
            'update': { method:'PUT' }
        });
    });
