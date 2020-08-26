<template>
	<div id="login">
        <div id="loginForm">
            <h1>Login</h1>
            <h2>{{successMsg}}</h2>
            <floating-label placeholder="Username" name="username"></floating-label>
            <floating-label placeholder="Password" name="password"></floating-label>
             <div class="loginButtons">
                <button class="button-primary">Log in</button>
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
                user: {},
                errors: [],
                successMsg: "",
                loggedIn: false,
            }
        },
        methods:
        {
            login: function()
            {
                // TODO(Jovan): Validation
                // TODO(Jovan): Cuvanje u memoriji preko refresh tokena, umesto u local

                axios.post("rest/login", this.user)
                    .then(response =>
                    {
                        this.successMsg = "Logged in!";
                        localStorage.jwt = response.data;
                    })
                    .catch(response =>
                    {
                        this.successMsg = "Failed to log in!";
                    });
            },
            verify: function()
            {
                var jwt = localStorage.jwt;
                if(!jwt)
                {
                    // NOTE(Jovan): Ako ne postoji jwt, ne pokusavaj login
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
</style>
