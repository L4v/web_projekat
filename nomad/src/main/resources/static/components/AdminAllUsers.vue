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
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                users: [],
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

            }
        },
        mounted()
        {
            this.getUsers();
        }
    }
</script>

<style>

</style>