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
	       		<button class="button-primary" @click="sortReservations()">Sort</button>
	       	</div>
	       	<br>
			<table>
				<tr>
					<th>Start date</th>
					<th>Number of days</th>
					<th>Total price</th>
					<th>Status</th>
					<th>Cancel reservation</th>
				</tr>
				 <tr v-for="reservation in reservations">
					<td>{{reservation.startDate}}</td>
					<td>{{reservation.noDays}}</td>
					<td>{{reservation.totalPrice}}</td>
					<td>{{reservation.status}}</td>
					<td v-if="reservation.status === 'ACCEPTED' || reservation.status === 'CREATED'"><button class="button-primary" @click="cancelReservation(reservation)">Cancel</button></td>
					<td v-else><b class="error">Cancelled</b></td>
				</tr>
				<tr>
					{{successMsg}}
				</tr>
			</table>
		</div>
		<button v-if="!showForm" type="button" @click="showForm = !showForm">+</button>
		<div v-if="showForm" id="add-reservation-form">
			<select id="selected-apartment" name="apartment" v-model="selectedApartment" required>
				<option value="" disabled>Select apartment</option>
				<option v-for="apartment in apartments" :value="apartment">{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</option>
			</select>
			<div v-if="selectedApartment" id="apartment-info">
				<div id="apartment-info-text">
					<h2>Apartment location</h2>
					<div id="apartment-info-text-container">
						<floating-label readonly :inputdata.sync="selectedApartment.location.address.street" placeholder="Street" type="text" name="street"></floating-label>
						<floating-label readonly :inputdata.sync="selectedApartment.location.address.streetNumber" placeholder="Street No." type="text" name="streetNo"></floating-label>
						<floating-label readonly :inputdata.sync="selectedApartment.location.address.area" placeholder="Area" type="text" name="area"></floating-label>
						<floating-label readonly :inputdata.sync="selectedApartment.location.address.areaCode" placeholder="Area Code" type="text" name="areaCode"></floating-label>
					</div>
				</div>
				<map-small :lat="selectedApartment.location.lat" :lon="selectedApartment.location.lon" :zoom="18" :setmarker="true"></map-small>
				<div id="apartment-info-date-amenities">
					<div id="apartment-info-date">
						<h2>Reservation dates</h2>
						<div id="apartment-info-date-container">
							<!-- TODO(Jovan): More practical to use date range instead of typing number of days? -->
							<!-- <v-date-picker is-inline mode="range" v-model="reservationDate" :available-dates="selectedApartment.availableDates"></v-date-picker> -->
								<small class="error">{{errors.reservationDate}}</small>
								<v-date-picker is-inline v-model="reservationDate" :available-dates="selectedApartment.availableDates"></v-date-picker>
								<!-- TODO(Jovan): Validate on input update? -->
								<small class="error">{{errors.noDays}}</small>
								<floating-label v-if="reservationDate" type="number" name="noDays" placeholder="Reservation length" :inputdata.sync="noDays"></floating-label>
						</div>
					</div>
					<div id="apartment-info-amenities">
						<h2>Amenities</h2>
						<div id="apartment-info-amenities-container">
							<table>
								<tr v-for="amenity in selectedApartment.amenities">
									<td>{{amenity.name}}</td>
									<td><i class="fa fa-check" aria-hidden="true"></i></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<small class="error">{{errors.reservationMessage}}</small>
				<textarea name="reservationMessage" id="reservation-message" placeholder="Write your message to the host" cols="100" rows="16" v-model="reservationMessage"></textarea>
				<b class="error">{{addingReservationMsg}}</b>
				<button type="button" class="button-primary" @click="addReservation">Add reservation</button>

			</div>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				reservations: {},
				sort: "",
				successMsg: "",
				addingReservationMsg: "",
				showForm: false,
				apartments: [],
				selectedApartment: "",
				apartmentId: "",
				reservationDate: null,
				noDays: 0,
				reservationMessage: "",
				errors:
				{
					reservationDate:    "",
					noDays:             "",
					reservationMessage: "",
				},

			}
		},
		
		methods:
		{
			validateReservationDate: function()
			{
				if(!this.reservationDate)
				{
					this.errors.reservationDate = "Please choose a reservation date";
					return false;
				}
				return true;
			},

			validateNoDays: function()
			{
				if(!this.noDays || this.noDays <= 0)
				{
					// TODO(Jovan): Auto fix it?
					this.errors.noDays = "Must be a positive integer";
					return false;
				}

				// NOTE(Jovan): Add potential reservation length to the first day 
				/*let lastDay = new Date(this.reservationDate.getTime());
				console.log("Before addition: " + lastDay.getTime());
				lastDay.setDate(lastDay.getDate() + this.noDays);*/

				let lastDay = new Date(this.reservationDate.getTime());
				lastDay.setDate(lastDay.getDate() + parseInt(this.noDays));

				// NOTE(Jovan): Find in which interval of available dates it belongs to
				for(let range of this.selectedApartment.availableDates)
				{
					if(lastDay.getTime() >= range.start.getTime() && lastDay.getTime() <= range.end.getTime())
					{
						// NOTE(Jovan): Number of days added to the first day
						// fits inside an interval -> valid noDays
						return true;
					}
				}

				this.errors.noDays = "Reservation length is too long";
				return false;
			},

			validateReservationMesssage: function()
			{
				if(!this.reservationMessage)
				{
					this.errors.reservationMessage = "Must not be empty";
					return false;
				}

				return true;
			},

			validateAllInputs: function()
			{
				this.errors =
				{
					reservationDate:    "",
					noDays:             "",
					reservationMessage: "",
				};

				let reservationDateValid = this.validateReservationDate();
				let noDaysValid = this.validateNoDays();
				let reservationMessageValid = this.validateReservationMesssage();

				return reservationDateValid && noDaysValid && reservationMessageValid;
			},

			addReservation: function()
			{
				if(!this.validateAllInputs())
				{
					console.log("Invalid inputs");
					return;
				}

				let reservation = 
				{
					apartmentId: 		this.selectedApartment.id,
					startDate:   		this.reservationDate,
					noDays:      		this.noDays,
					totalPrice:  		0,
					reservationMessage: this.reservationMessage,
					guestId:            "",
					status:             "",
					id:					"",
				};

				let jwt = localStorage.jwt;
				if(!jwt)
				{
					this.$router.go();
					return;
				}

				axios.post("rest/create_reservation", reservation, {headers: {"Authorization": "Bearer " + jwt}})
					.then(response => 
					{
						this.addingReservationMsg = "Reservation added";
						this.$router.go();
					})
					.catch(response =>
					{
						this.addingReservationMsg = "Failed to add reservation";
					});
			},

			// NOTE(Jovan): Gets all available apartments
			getApartments: function()
			{
				console.log("Getting apartments");
				let jwt = localStorage.jwt;
				if(!jwt)
				{
					// TODO(Jovan): Handle?
					console.log("No jwt");
					return;
				}
				axios.get("rest/guest_all_apartments", {headers: {"Authorization": "Bearer " + jwt}})
					.then(response =>
					{
						this.apartments = response.data;
						/* NOTE(Jovan):
						*	Dates are acting weird, parsing them from java to js
						*	messes them up. This is the only fix that works for some reason
						*/
						this.apartments.forEach(a => 
						{
							let datesClone = [];
							a.availableDates.forEach(date =>
							{
								datesClone.push({start: new Date(date.start), end: new Date(date.end)});
							});
							a.availableDates = [];
							a.availableDates = [...datesClone];
						});
					})
					.catch(response =>
					{
						// TODO(Jovan): Handle;
					});

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
		
			cancelReservation: function(reservation)
			{
				axios.post("rest/guest_cancel_reservation", reservation, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
				.then(response =>
					{
						this.reservations = response.data;
						this.successMsg = "Reservation successfully cancelled.";
					})
				.catch(response => {
						this.successMsg = "Failed canceling reservation";
				});
			}
		},

		mounted()
		{
			var jwt = localStorage.jwt;
			if(!jwt)
			{
				alert("Please log in");
				return;
			}
			if(jwt)
			{
				axios.get("rest/guest_view_reservations",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
					.then(response => (this.reservations = response.data))
					.catch(response => 
					{
						//TODO(Kristian): handle 404
					});
			}
			this.getApartments();
		},
	}
</script>

<style scoped>
	h2
	{
		font-size: 2rem;
	}
	#selected-apartment
	{
		width: 100%;
	}

	#apartment-info
	{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	#apartment-info-text
	{
		display: flex;
		flex-direction: column;
	}

	#apartment-info-text-container
	{
		display: grid;
		grid-template-columns: auto auto;
		padding: 20px;
	}

	#apartment-info-date
	{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
	}

	#apartment-info-date-container
	{
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
	}

	#apartment-info-date-amenities
	{
		display: grid;
		grid-template-columns: 1fr 1fr;
		padding: 20px;
	}

	#apartment-info-amenities
	{
		text-align: center;
	}

	#apartment-info-amenities-container
	{
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.error
	{
		color: #f00;
		padding-bottom: 8px;
		font-weight: 500;
	}

	.fa-check
	{
		color: #0f0;
	}
</style>