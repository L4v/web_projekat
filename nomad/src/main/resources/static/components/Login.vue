<template>
	<form id="login" @submit.prevent="login">
        <div id="loginForm">
            <h1>Login</h1>
            <b class="error">{{errors.login}}</b>
            <small class="error">{{errors.username}}</small>
            <floating-label :inputdata.sync="user.username" placeholder="Username" name="username" type="text"></floating-label>
            <small class="error">{{errors.password}}</small>
            <floating-label :inputdata.sync="user.password" placeholder="Password" name="password" type="password"></floating-label>
             <div class="loginButtons">
                <button class="button-primary" @click="login()">Log in</button>
                <button>Forgot password?</button>
             </div>
        </div> 
	</form>
</template>

<script>
    module.exports = 
    {
        data: function()
        {
            return{
                user: {username:"", password:""},
                loggedIn: false,
                errors: {username: "", password: "", login: ""},
            }
        },
        methods:
        {
            validateUsername: function()
            {
                if(!this.user.username)
                {
                    this.errors.username = "Username must not be empty";
                    return false;
                }
                let regex = /^[A-Za-z]+/;
                if(!regex.test(this.user.username))
                {
                    this.errors.username = "Username must contain alphabet letters only";
                    return false;
                }
                return true;
            },
            validatePassword: function()
            {
                if(!this.user.password)
                {
                    this.errors.password = "Password must not be empty";
                    return false;
                }
                if(this.user.password.length < 8)
                {
                    this.errors.password = "Password must be at least 8 characters long";
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                if(!regex.test(this.user.password))
                {
                    this.errors.password = "Password can contain alphabet letters, numbers and !@#$%^&* only";
                    return false;
                }
                return true;
            },
            validateFields: function()
            {
                this.errors.username = "";
                this.errors.password = "";
                this.errors.login = "";
                return this.validateUsername()
                    && this.validatePassword();
            },
            login: function()
            {
                // TODO(Jovan): Using refresh tokens instead of localStorage

                if(!this.validateFields())
                {
                    return;
                }
                axios.post("rest/login", this.user)
                    .then(response =>
                    {
                        localStorage.jwt = response.data;
                        this.loggedIn = true;
                        // NOTE(Jovan): Go back one page
                        this.$router.push("/");
                    })
                    .catch(response =>
                    {
                        this.errors.login = "Invalid username or password";
                    });
            },
            verify: function()
            {
                var jwt = localStorage.jwt;
                if(!jwt)
                {
                    // NOTE(Jovan): If jwt doesn't exist, don't attempt login
                    return;
                }
                axios.get("rest/test", {headers:{"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        this.loggedIn = true;
                    })
                    .catch(response =>
                    {
                        this.loggedIn = false;
                    });
            },
        }
    }
</script>

<style scoped>
    #login > input[type=text],
    #login > input[type=password]
    {
        width: 100%;
        padding: 12px 20px;
        margin: 8px 0;
        display: inline-block;
        border: 1px solid #ccc;
        box-sizing: border-box;
    }


    #login
    {
        position: fixed;
        width: 100vw;
        height: 100vh;
        background: none;
        background-color: rgb(245, 245, 245);
    }

    #loginForm
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        margin: auto;
        margin-top: 5vh;
        width: 25%;
        height: 60vh;

        border: 1px solid rgba(127, 127, 127, 0.5);
        border-radius: 20px;

        background: none;
        background-color: #fff;
        box-shadow: 0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23);
    }
    
    .error
    {
        color: #f00;
        padding: 0;
        padding-bottom: 8px;
        margin: 0;
        font-weight: 500;
    }

    .loginField 
    {
        width: 80%;
    }

    .loginField input
    {
        width: 100%;
    }

    .loginButtons
    {
        display: flex;
        flex-direction: column;
        width: 80%;
    }

</style>
