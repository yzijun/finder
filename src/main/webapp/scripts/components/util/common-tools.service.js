'use strict';

angular.module('finderApp')
    .service('CommonTools', function () {
		  // 回到顶部按钮实现函数
	      this.scrollTo = function(name, add, speed) {
		  	    if (!speed) speed = 300
			    if (!name) {
			        $('html,body').animate({
			            scrollTop: 0
			        }, speed)
			    } else {
			        if ($(name).length > 0) {
			            $('html,body').animate({
			                scrollTop: $(name).offset().top + (add || 0)
			            }, speed)
			        }
			    }
		};
		// 是否显示回到顶部按钮
        this.showToTop = function() {
        	$(window).scroll(
				function() {
					$(this).scrollTop() > 400 ? $("#go-top-btn").css(
									"display", "block") : $("#go-top-btn").hide()
			});
		};
     
    });
