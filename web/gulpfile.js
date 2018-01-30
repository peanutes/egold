var config = require('./gulp_config'),
	gulp = require('gulp'),
	gulpSequence = require('gulp-sequence'),
	less = require('gulp-less'),
	autoprefixer = require('gulp-autoprefixer'),
	postcss = require('gulp-postcss'),
	px2rem = require('postcss-px2rem'),
	clean = require('gulp-clean'),
	notify = require("gulp-notify"),
	plumber = require("gulp-plumber"),
	cleanCSS = require('gulp-clean-css');

var watchingFilesPc = config.pcLessFiles,
	watchingFilesWap = config.wapLessFiles;

gulp.task('default', function () {

	gulp.watch([watchingFilesPc, watchingFilesWap]).on('change', function () {
		gulp.start(gulpSequence(['less_pc', 'less_wap','minify-css'], function () {
			console.log(".....................Watching directory " + watchingFilesPc);
			console.log(".....................Watching directory " + watchingFilesWap);
		}));

		//gulp.start("minify-css");

	});

});

gulp.task('less_wap', function () {

	gulp.watch(watchingFilesWap).on('change', function () {
		gulp.start(gulpSequence('less_wap', function () {
			console.log(".....................Watching directory " + watchingFilesWap);
		}));

		gulp.start("minify-css");
	});

});

gulp.task('less_pc', function () {

	gulp.watch(watchingFilesPc).on('change', function () {
		gulp.start(gulpSequence('less_pc', function () {
			console.log(".....................Watching directory " + watchingFilesPc);
		}));

	});

});

gulp.task('less_wap', function () {

	var processors = [px2rem({remUnit: 75})];

	return gulp.src(config.wapLessFiles)
		.pipe(plumber({
			errorHandler: function (err) {
				notify.onError({
					title: "Gulp Error",
					message: "Error: <%= error.message %>",
					sound: "Bottle"
				})(err);

				this.emit('end');
			}
		}))
		.pipe(less()).pipe(autoprefixer({
			browsers: ['>= 0.5%', 'Android >= 4.0', 'last 5 versions'],
			cascade: true
		}))
		.pipe(postcss(processors))
		.pipe(gulp.dest(config.cssDir))
	//.pipe(notify({
	//	message: 'less文件编译成功'
	//}))

});

gulp.task('less_pc', function () {

	return gulp.src(config.pcLessFiles)
		.pipe(plumber({
			errorHandler: function (err) {
				notify.onError({
					title: "Gulp Error",
					message: "Error: <%= error.message %>",
					sound: "Bottle"
				})(err);

				this.emit('end');
			}
		}))
		.pipe(less()).pipe(autoprefixer({
			browsers: ['>= 0.5%', 'Android >= 4.0', 'last 5 versions'],
			cascade: true
		}))
		.pipe(gulp.dest(config.cssDir))
	//.pipe(notify({
	//	//message: 'less文件编译成功'
	//	message: 'less文件编译成功'
	//}))

});

gulp.task('minify-css', function () {
	console.log(".....................minute " + config.cssDir);

	return gulp.src(config.cssDir + "/*.css").pipe(cleanCSS({debug: true}, function (details) {
		console.log(details.name + ' (before:' + details.stats.originalSize + ", after:" + details.stats.minifiedSize + ")");
	}))
		.pipe(gulp.dest(config.cssDir))
});
