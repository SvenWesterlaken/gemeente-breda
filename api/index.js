/**
 * Created by Koen on 11-5-2017.
 */

var http = require('http');
var express = require('express');
var config = require('./config.json');

var app = express();

app.set('PORT', config.webPort);

app.all('*', function(req, res, next){
    res.status(200);
    res.json({});
    next();
});

app.use('/apiv1', require('./routes/routes_apiv1'));

var port = process.env.PORT || app.get('PORT');

app.listen(port, function() {
    console.log('Port: ' + port);
});
