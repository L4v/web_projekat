<template>
    <!-- NOTE(Jovan):
        Input field with floating placeholders
        
        options:
            placeholder - placeholder text
            name        - defines input name
            type        - defines input type (password or text)
    -->
    <div class="loginField floating-label" >
        <label for="username">{{placeholder}}</label>
        <input 
            v-bind:name="name"
            v-bind:type="type"
            v-bind:value="value"
            v-model="content"
            @blur="removeFocus($event)"
            @focus="addFocus($event)"
            v-on:input="isEmpty($event); $emit('input', $event.target.value)"
            />
    </div>
</template>
<script>
    module.exports = {
        props: ["placeholder", "name", "type", "value"],
        data: function()
        {
            return {
                content: "",
            }
        },
        methods:
        {
            addFocus: function(e)
            {
                let classList = e.target.parentElement.classList;
                if(!classList.contains('focused'))
                {
                    classList.toggle('focused');
                }
            },
            removeFocus: function(e)
            {
                let classList = e.target.parentElement.classList;
                if(classList.contains('focused') && this.isEmpty())
                {
                    classList.toggle('focused');
                }
            },
            isEmpty: function(e)
            {
                if(this.content)
                {
                    return false;
                }
                return true;
            }
        },
    }
</script>
<style scoped>

    .floating-label
    {
        position: relative;
        margin-bottom: 10px;
    }

    .floating-label label
    {
        transition: all .2s ease-in-ease-out;
        font-size: 1.5rem;
        pointer-events: none;
        position: absolute;
        white-space: nowrap;
        overflow: hidden;
        top: 0;
        display: inline-block;
        background: #fff;
        margin: 8px 5px;
        padding: 0 10px;
        opacity: 0.6;
        font-weight: 500;
		text-transform: capitalize;
    }

    .focused label
    {
        transition: all .2s linear;
        opacity: 1;
        color: #ff5722;
        top: -20px;
        font-size: 1.1rem;
		text-transform: uppercase;
    }
</style>
