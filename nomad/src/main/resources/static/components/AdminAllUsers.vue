<template>
    <div>
        <ul>
            <li v-for="user in users">{{user.username}}</li>
        </ul>
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