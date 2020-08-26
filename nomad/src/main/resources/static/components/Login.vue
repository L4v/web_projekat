<template>
	<div id="login">
		<h1>Login</h1>
		<h2>{{successMsg}}</h2>
		<table id="tabela">
			<tr>
				<td>Username:</td>
				<td><input type="text" id="username" v-model="user.username"
					placeholder="Username"></td>
				<td id="emptyUsername">Enter your username</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" id="password"
					v-model="user.password" placeholder="Password"></td>
				<td id="emptyPassword">Enter your password</td>
			</tr>
			<tr>
				<td colspan="2"><button v-on:click="login()">Log in</button></td>
			</tr>
		</table>
		<h2>Verify login: {{loggedIn}}</h2>
		<button v-on:click="verify()">Test</button>
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
    table, tr, td {
        border: none;
    }

    #emptyUsername, #emptyPassword {
        color: red;
    }

    #loginButton {
        align-items: center;
    }
</style>
