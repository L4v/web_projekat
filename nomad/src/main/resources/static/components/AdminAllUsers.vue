<template>
    <div class="container">
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>Surname</th>
                <th>User Type</th>
            </tr>
            <tr v-for="user in users">
                <td>{{user.username}}</td>
                <td>{{user.name}}</td>
                <td>{{user.surname}}</td>
                <td>{{user.userType}}</td>
            </tr>
        </table>
        <!-- TODO(Jovan): This is temporary, will add correct form later -->
        <floating-label type="text" name="username"  placeholder="Username" :inputdata.sync="host.username"></floating-label>
        <floating-label type="text" name="password"  placeholder="Password" :inputdata.sync="host.password"></floating-label>
        <floating-label type="text" name="name"  placeholder="Name" :inputdata.sync="host.name"></floating-label>
        <floating-label type="text" name="surname"  placeholder="Surname" :inputdata.sync="host.surname"></floating-label>
        <button class="button-primary" @click="registerHost">Add new host</button>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                users: [],
                host: 
                    {
                        username: "",
                        password: "",
                        name:     "",
                        surname:  "",
                        sex:      "MALE",
                    },
            }
        },
        methods:
        {
            getUsers: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    // TODO(Jovan): Handle login redirect?
                    return;
                }
                axios.get("rest/admin_all_users", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        this.users = response.data;
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle failure
                    });
            },
            /* TODO(Jovan): Finish
            registerHost: function()
            {
                axios.post("rest/reg_host", this.host)
            },
            */
        },
        mounted()
        {
            this.getUsers();
        }
    }
</script>

<style>

</style>