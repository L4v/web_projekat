<template>
	<div class="container">
		<div id="reservations">
			<h1>Reservations</h1>
			<br>
			<div id="sorting">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortReservations()>Sort</button>
	       	</div>
	       	<div id="filter">
	       		Filter by status:
	       		<select name="filter" v-model="filter" required>
	            	<option value="" disabled>Filter</option>
	            	<option value="ALL">ALL</option>
	            	<option value="CREATED">CREATED</option>
	            	<option value="REJECTED">REJECTED</option>
	            	<option value="CANCELLED">CANCELLED</option>
	            	<option value="ACCEPTED">ACCEPTED</option>
	            	<option value="FINISHED">FINISHED</option>
	       		</select>
	       		<button class="button-primary" @click=filterReservations()>Filter</button>
	       	</div>
	       	<div id="search">
	       		Search by username:
	       		<input v-model="username" placeholder="Username..." type="text">
	       		<button class="button-primary" @click=searchReservation()>Search</button>
	       	</div>
	       	<br>
			<table>
				<tr>
					<th>Apartment address</th>
					<th>Start date</th>
					<th>No of days</th>
					<th>Total price</th>
					<th>Status</th>
					<th>Guest username</th>
				</tr>
				 <tr v-for="reservation in reservations">
				 	<td>{{reservation.apartment.location.address.street}} {{reservation.apartment.location.address.streetNumber}}  {{reservation.apartment.location.address.area}}</td>
					<td>{{reservation.reservation.startDate}}</td>
					<td>{{reservation.reservation.noDays}}</td>
					<td>{{reservation.reservation.totalPrice}}</td>
					<td>{{reservation.reservation.status}}</td>
					<td>{{reservation.reservation.guestId}}</td>
				</tr>
			</table>
			{{successMsg}}
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				reservations: [],
				reservations_copy: [],
				sort: "",
				filter: "",
				username: "",
				successMsg: "",
			}
		},
		
		mounted()
		{
			 this.getReservationsPromise()
                .then(response =>
                {
                    let apartmentIds = [];
                    response.forEach(reservation =>
                    {
                        this.reservations.push(
                            {
                                reservation: reservation,
                                apartment:   {},
                            }
                        );
                        apartmentIds.push(reservation.apartmentId);
                    });
                    axios.get("rest/admin_all_apartments",
                    {
                        headers:
                        {
                            "Authorization": "Bearer " + localStorage.jwt,
                        },
                    }).then(response =>
                    {
                        response.data.forEach(apartment =>
                        {
                            for(let reservation of this.reservations)
                            {
                                if(reservation.reservation.apartmentId === apartment.id)
                                {
                                    reservation.apartment = apartment;
                                }
                            }
                        });
                    });
                });
	    },
	    
	    methods:
	    {
	    	getReservationsPromise: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }
                return axios.get("rest/admin_view_reservations", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => response.data);
            },
	    
	    
	    
	    	sortReservations: function()
	    	{
	    		if(!this.sort)
	    		{
	    			this.successMsg = "Sort parameter must not be empty.";
	    		} 
	    		else if(this.sort == 'DESCENDING')
	    		{
	    			this.reservations.sort((a, b) => (a.totalPrice > b.totalPrice) ? 1 : -1);
	    		}
	    		else
	    		{
	    			this.reservations.sort((a, b) => (a.totalPrice < b.totalPrice) ? 1 : -1);
	    		}	
	    	},
	    	
	    	filterReservations: function()
	    	{
	    		if(!this.filter)
	    		{
	    			this.successMsg = "Filter parameter must not be empty.";
	    		} 
	    		else if(this.filter == 'ALL')
	    		{
	    			this.reservations = this.reservations_copy;
	    		}
	    		else
	    		{
		    		var retVal = [];
		    		for(reservation of this.reservations_copy)
		    		{
		    			if(reservation.status == this.filter)
		    			{
		    				retVal.push(reservation);
		    			}
		    		}
		    		this.reservations = retVal;
	    		}
	    	},
	    	
	    	searchReservation: function()
	    	{
	    		if(!this.username)
	    		{
	    			this.reservations = this.reservations_copy;
	    		}
	    		else
	    		{
	    			//TODO
	    		}
	    	}
	    },
	}
</script>