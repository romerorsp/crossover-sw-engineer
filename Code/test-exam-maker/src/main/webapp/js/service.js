'use strict';

angular
    .module('exporter')
    .service('myservice', [ 'Restangular', '$q', 'messagesBusService', function(Restangular, $q, messagesBusService){

        var user = {
            username: null,
            idUser: null
        };

        var production = {};
        var product = {};
        var cvlacCredentials = {
            valid: false
        };

        /* ------------------------------------------------------------------------------
        *           RESOURCES
        * ------------------------------------------------------------------------------- */



        /* ------------------------------------------------------------------------------
         *           SERVICE FUNCTIONS
         * ------------------------------------------------------------------------------- */

        return {

            /**
             * Realiza el login del usuario contra el backend
             * @param username
             * @param password
             * @param acceptConditions
             * @returns {*}
             */
            logIn : function(username, password, acceptConditions){
                console.log('myservice.logIn('+username+', '+acceptConditions+')');

                var loginResource = Restangular.all('login');

                var credentials = {
                    username : username,
                    password : password,
                    acceptConditions : acceptConditions
                };

                var deferred = $q.defer();
                var answer = new restResponse();

                loginResource.post(credentials).then(function(response) {
                    console.log('myservice.login() OK :' + response.data.idUser);
                    user.idUser = response.data.idUser;
                    user.username = username;
                    answer.handleSuccessfullResponse(response, response.data);

                    deferred.resolve(answer);
                },function(response) {
                    console.log('myservice.login() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            isActiveSession : function(){
                if(typeof user.idUser === 'number'){
                    if(user.idUser >= 0){
                        return true;
                    }
                }
                return false;
            },

            /**
             * Retorna el username del usuario logeado en el sistema
             * @returns {null}
             */
            getUsername: function(){
                return user.username;
            },

            /**
             * Cierra la sesión del usuario con el backend
             * @returns {*}
             */
            logOut : function(){
                console.log('invoking logout');
                var loginResource = Restangular.all('login');

                var deferred = $q.defer();
                var answer = new restResponse();

                this.cleanSession();

                loginResource.remove().then(function(response){

                    answer.handleSuccessfullResponse(response, response.data);
                    deferred.resolve(answer);
                }, function(response){
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            cleanSession : function(){
                user = {};
                cvlacCredentials.valid = false;
            },

            /**
             * Consulta los productos del usuario que esta logeado en el sistema
             * @param start
             * @param limit
             * @returns {*}
             */
            searchProducts : function(start, limit) {
                console.log('MyService.searchProduct('+user.idUser+', '+start+', '+limit+')');

                var deferred = $q.defer();
                var answer = new restResponse();

                /* GET /professor/123/product?start=x&limit=y */
                Restangular.one('professor', user.idUser).customGET('product', {start: start, limit: limit}).then(function(response){
                    console.log('MyService.searchProduct() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    production = response.data;
                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.searchProduct() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            /**
             *
             * @param pub
             */
            selectProduct : function (pub) {
                console.log('MyService.selectPublication()');
                product = pub;
            },

            /**
             *
             * @returns {{}}
             */
            getProductSelected: function(){
                return product;
            },

            /**
             *
             * @returns {boolean}
             */
            validCvlacCredentials : function(){
                return cvlacCredentials.valid;
            },

            /**
             *
             * @param credentials
             * @returns {*}
             */
            authenticateWithCvlac : function(credentials) {
                console.log('MyService.authenticateWithCvlac(' + credentials.name + ', ' + credentials.docNumber + ', )');

                var researcherResource = Restangular.all('cvlac');

                cvlacCredentials.name = credentials.name;
                cvlacCredentials.docNumber = credentials.docNumber;
                cvlacCredentials.password = credentials.password;

                var deferred = $q.defer();
                var answer = new restResponse();

                /* POST /cvlac/validate */
                researcherResource.customPOST(credentials, 'validate').then(function(response){
                    console.log('MyService.authenticateWithCvlac() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    cvlacCredentials.valid = true;

                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.authenticateWithCvlac() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    cvlacCredentials.valid = false;
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            /**
             *
             * @param title
             * @param type
             * @returns {*}
             */
            searchInCvlac : function(title, type) {
                console.log('MyService.searchInCvlac('+title+', '+type+')');

                var researcherResource = Restangular.all('cvlac');

                var filters = {
                    credentials: {
                        name: cvlacCredentials.name,
                        docNumber: cvlacCredentials.docNumber,
                        password: cvlacCredentials.password
                    }
                }

                var deferred = $q.defer();
                var answer = new restResponse();

                /* POST /cvlac/product */
                researcherResource.customPOST(filters, 'product', {query: title, type: type}).then(function(response){
                    console.log('MyService.searchInCvlac() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.searchInCvlac() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            /**
             *
             * @param uniProduct
             * @param cvlacProduct
             * @returns {*}
             */
            associate2cvlac : function(uniProduct, cvlacProduct){
                console.log('MyService.associate2cvlac('+user.idUser+', '+uniProduct.idUniversitasProduct+', '+uniProduct.type+', '+cvlacProduct.idCvlacProduct+', '+cvlacProduct.codRh+')');

                var deferred = $q.defer();
                var answer = new restResponse();

                var prod = {
                    credentials: {
                        name: cvlacCredentials.name,
                        docNumber: cvlacCredentials.docNumber,
                        password: cvlacCredentials.password
                    },
                    idCvlacProduct: cvlacProduct.idCvlacProduct,
                    codRh: cvlacProduct.codRh,
                    name: cvlacProduct.name,
                    year: cvlacProduct.year,
                    type: uniProduct.type.id,
                    idUniversitasProduct: uniProduct.idUniversitasProduct
                };


                /* POST /professor/{idUser}/product/associate */
                Restangular.one('professor', user.idUser).customPOST(prod, 'product/associate').then(function(response){
                    console.log('MyService.associate2cvlac() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.associate2cvlac() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            /**
             *
             * @param idUser
             * @param idUniversitas
             * @param type
             */
            export2cvlac : function(uniProduct){
                console.log('MyService.export2cvlac('+user.idUser+', '+uniProduct.idUniversitasProduct+', '+uniProduct.type+')');

                var bodyRest = {
                    credentials: {
                        name: cvlacCredentials.name,
                        docNumber: cvlacCredentials.docNumber,
                        password: cvlacCredentials.password
                    },
                    type: uniProduct.type.id,
                    name: uniProduct.name,
                    idUniversitasProduct: uniProduct.idUniversitasProduct
                };

                var deferred = $q.defer();
                var answer = new restResponse();

                /* POST /professor/{idUser}/product/export */
                Restangular.one('professor', user.idUser).customPOST(bodyRest, 'product/export').then(function(response){
                    console.log('MyService.export2cvlac() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.export2cvlac() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            },

            /**
             *
             * @param idUser
             * @param idexport
             */
            reexport2cvlac : function(expProduct){
                console.log('MyService.reexport2cvlac('+user.idUser+', '+expProduct.idExporterProduct+')');

                var bodyRest = {
                    name: cvlacCredentials.name,
                    docNumber: cvlacCredentials.docNumber,
                    password: cvlacCredentials.password
                };

                var deferred = $q.defer();
                var answer = new restResponse();

                /* POST /professor/{idUser}/exportproduct/{idProduct}/reexport */
                Restangular.one('professor', user.idUser).one('exportproduct', expProduct.idExporterProduct).customPOST(bodyRest, 'reexport').then(function(response){
                    console.log('MyService.reexport2cvlac() OK');
                    answer.handleSuccessfullResponse(response, response.data);
                    deferred.resolve(answer);
                },function(response){
                    console.log('MyService.reexport2cvlac() ERROR');
                    answer.handleErrorResponse(response, response.data);
                    deferred.reject(answer);
                });

                return deferred.promise;
            }

        };

    }]);


angular
    .module('exporter')
    .service('alertService', [ function(){

        var alert = {
            show : false,
            style: 'success',
            icon: 'check',
            color: '#3c763d',
            statusCode: '(200)',
            message: ''
        };

        /* ------------------------------------------------------------------------------
         *           SERVICE FUNCTIONS
         * ------------------------------------------------------------------------------- */

        return {
            showDanger : function(msg, status){
                alert.show = true;
                this.setMessage(msg, status);
                this.show(MESSAGE_TYPE.DANGER);
            },

            showWarning : function(msg, status){
                alert.show = true;
                this.setMessage(msg, status);
                this.show(MESSAGE_TYPE.WARNING);
            },

            showSuccess : function(msg){
                alert.show = true;
                this.setMessage(msg, undefined);
                this.show(MESSAGE_TYPE.SUCCESS);
            },

            showInfo : function(msg){
                alert.show = true;
                this.setMessage(msg);
                this.show(MESSAGE_TYPE.INFO);
            },

            show : function(type){
                alert.style = type.style;
                alert.icon =  type.icon;
                alert.color = type.color;
            },

            visible : function(){
                return alert.show;
            },

            hide : function(){
                alert.show = false;
                alert.message =  '';
            },

            setMessage : function(msg, status){
                if(status === undefined){
                    alert.message =  msg;
                }
                else{
                    alert.message =  '(' + status + ') ' + msg;
                }

                alert.statusCode = status;
            },

            getAlert : function(){
                return alert;
            }
        };

    }]);