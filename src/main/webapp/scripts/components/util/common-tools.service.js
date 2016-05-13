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
//			                scrollTop: $(name).offset().top + (add || 0)
			            	// 获取浏览器显示区域的高度  $(window).height();
			                scrollTop: $(name).offset().top + $(window).height()
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
		// 显示JHipster QQ群
		this.showQQGroup = function() {
	    	$(".js-jhipster-qq").hover(function(){
	    		$(this).find("div:first").fadeIn('slow');
	    	},function(){
	    		$(this).find("div:first").fadeOut('slow');
	    	});
		};
		// 去掉所有html标记的函数
        this.delHtmlTag = function(str) {
        	// 去掉网页中的所有的html标记
        	return str.replace(/<[^>]+>/g,"").replace(/&nbsp;/g,"");
        };
    });
