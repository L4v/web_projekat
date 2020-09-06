<template>
	<div id="login">
        <div id="loginForm">
            <h1>Login</h1>
            <b :class="loggedIn ? 'success' : 'failure'">{{successMsg}}</b>
            <floating-label :inputdata.sync="user.username" placeholder="Username" name="username" type="text"></floating-label>
            <small>{{errors.username}}</small>
            <floating-label :inputdata.sync="user.password" placeholder="Password" name="password" type="password"></floating-label>
            <small>{{errors.password}}</small>
             <div class="loginButtons">
                <button class="button-primary" @click="login()">Log in</button>
                <button>Forgot password?</button>
             </div>
        </div> 
	</div>
</template>

<script>
    module.exports = 
    {
        data: function()
        {
            return{
                user: {username:"", password:""},
                successMsg: "",
                loggedIn: false,
                errors: {username: "", password: ""},
            }
        },
        methods:
        {
            validateUsername: function()
            {
                this.errors.username = "";
                if(!user.username)
                {
                    this.errors.username = "Username must not be empty";
                    return false;
                }
                let regex = /^[A-Za-z]+/;
                if(!regex.test(user.username))
                {
                    this.errors.username = "Username must contain alphabet letters only";
                    return false;
                }
            },
            login: function()
            {
                // TODO(Jovan): Using refresh tokens instead of localStorage

                axios.post("rest/login", this.user)
                    .then(response =>
                    {
                        this.successMsg = "Logged in!";
                        localStorage.jwt = response.data;
                        this.loggedIn = true;
                        // NOTE(Jovan): Go back one page
                        this.$router.go(-1);
                    })
                    .catch(response =>
                    {
                        this.successMsg = "Wrong username or password";
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

    .success,
    .failure
    {
        font-size: 1.5rem;
    }

    .success
    {
        color: #2ecc71;
    }

    .failure
    {
        color: #e74c3c;
    }
</style>
