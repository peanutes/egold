var
	staticDir = "./src/main/resources/static/",
	cssCompileTempDir=staticDir + "/cssCompileTempDir/",
	config = {
		staticDir:staticDir,
		pcLessFiles: staticDir +"pcLess/*.less",
		wapLessFiles: staticDir +"wapLess/*.less",
		cssDir:staticDir +"css/",
		autoprefixerCompileTempDir: cssCompileTempDir + "autoprefixer/",
		lessCompileTempDir: cssCompileTempDir + "less"//这里不能写成"less/"

	}

module.exports = config;
