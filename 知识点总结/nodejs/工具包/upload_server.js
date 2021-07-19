var fs = require('fs');
var express = require('express');
var multer  = require('multer')
var app = express();

var pass = '';
var uploadFolder = __dirname;

var createFolder = function(folder){
    try{
        fs.accessSync(folder); 
    }catch(e){
        fs.mkdirSync(folder);
    }  
};

createFolder(uploadFolder);

// 通过 filename 属性定制
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, uploadFolder);    // 保存的路径，备注：需要自己创建
    },
    filename: function (req, file, cb) {
        // 将保存文件名设置为 字段名 + 时间戳，比如 logo-1478521468943
        req.query.filename = req.query.filename || file.originalname + '-' + Date.now();
        cb(null, req.query.filename);  
    }
});

// 通过 storage 选项来对 上传行为 进行定制化
var upload = multer({ storage: storage })

app.use(function(req, res, next){
    if (pass && pass != '' && req.query.pass != pass) {
        return res.send({ret_code: '403'});
    }
    next();
})

// 单文件上传
app.post('/upload', upload.single('file'), function(req, res, next){
    console.log('上传文件：'+req.query.filename);
    res.send({ret_code: '0'});
});

app.listen(8887, function(){
    console.log('文件上传服务器启动');
});