(function () {
"use strict";

var app = angular.module('myApp', ['ngGrid', 'ngRoute']);

app.provider('globals', function () {
    if (!document.esupSmsuApiAdmin) alert("missing configuration document.esupSmsuApiAdmin");
    angular.extend(this, document.esupSmsuApiAdmin);
    this.$get = function () { return this; };
});

})();
