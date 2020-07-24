package com.example.springcrudl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightController {
    private final FlightRepository flightRepo;
    private final PilotRepository pilotRepo;
    private final PlaneRepository planeRepo;


    public FlightController(FlightRepository flightRepo, PilotRepository pilotRepo, PlaneRepository planeRepo) {
        this.flightRepo = flightRepo;
        this.pilotRepo = pilotRepo;
        this.planeRepo = planeRepo;
    }

    @GetMapping("/flights/all")
    public Iterable<Flight> allflights() {
        return this.flightRepo.findAll();
    }
    @PostMapping("/flights")
    public Flight create(@RequestBody Flight flight) {
        return this.flightRepo.save(flight);
    }
    @DeleteMapping("/flights/delete/{id}")
    public void deleteFlight(@PathVariable("id")Long id){
        this.flightRepo.deleteById(id);
    }
    @PatchMapping("/flights/patchPilot/{flightId}/{pilotId}")
    public Flight patch(@PathVariable("pilotId")Long pilotId,
                        @PathVariable("flightId")Long flightId){
        Flight flight = this.flightRepo.findById(flightId).get();
        Pilot newPilot = this.pilotRepo.findById(pilotId).get();

        Pilot oldPilot = flight.getPilot();

        oldPilot.getFlights().remove(flight);
        newPilot.getFlights().add(flight);

        flight.setPilot(newPilot);


        return this.flightRepo.save(flight);
    }
    @PatchMapping("/flights/patchFlights/{flightId}")
    public Flight patchFlight(
            @PathVariable("flightId")Long flightId,
            @RequestBody Flight change
    ){
        Flight flight = this.flightRepo.findById(flightId).get();
        if(change.getDate()!= null){
            flight.setDate(change.getDate());
        }
        if(change.getTime()!= null){
            flight.setTime(change.getTime());
        }
        return this.flightRepo.save(flight);
    }

    @PatchMapping("/flights/removePilot/{flightId}")
    public Flight patchDelete(@PathVariable("flightId")Long flightId){
        Flight flight = this.flightRepo.findById(flightId).get();
        flight.setPilot(null);
        return this.flightRepo.save(flight);

    }
    @GetMapping("/flights/dateQuery/")
    public List<Flight> queryByDate(@RequestParam String date) throws ParseException {
        return this.flightRepo.findByDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
    }
    @PatchMapping("/flights/addNotes/{flightId}")
    public Flight addNotes(@PathVariable("flightId")Long flightId,
                           @RequestBody String body){
        Flight flight = this.flightRepo.findById(flightId).get();
        flight.setNotes(body);
        return this.flightRepo.save(flight);
    }

    @GetMapping("/pilots/all")
    public Iterable<Pilot> allpilots() {
        return this.pilotRepo.findAll();
    }
    @PostMapping("/pilots")
    public Pilot create(@RequestBody Pilot pilot) {
        return this.pilotRepo.save(pilot);
    }
    @DeleteMapping("/pilots/delete/{id}")
    public void deletePilot(@PathVariable("id") Long id){
        this.pilotRepo.deleteById(id);
    }
    @PatchMapping("/pilots/randomAssign")
    public Iterable<Flight> randomAssign(){

        List<Flight> flights = this.flightRepo.findByPilotId(null);
        List<Pilot> pilots = (List<Pilot>) this.pilotRepo.findAll();

        int pilotCount = (int)this.pilotRepo.count();

        for(int i=0; i<flights.size();i++){
            Flight flight = flights.get(i);
            int random = (int) (Math.random()*pilotCount);
            Pilot pilot = pilots.get(random);
            flight.setPilot(pilot);

            this.flightRepo.save(flight);
        }
        return this.flightRepo.findAll();
    }
    @PostMapping("/planes")
    public Plane createPlane(@RequestBody Plane plane) {
        return this.planeRepo.save(plane);
    }
    @DeleteMapping("/planes/delete/{id}")
    public void deletePlane(@PathVariable("id")Long id){
        this.planeRepo.deleteById(id);
    }
    @PatchMapping("/flights/updatePlane/{flightId}/{planeId}")
    public Flight updatePlane(@PathVariable("flightId")Long flightId,
                              @PathVariable("planeId")Long planeId){
        Flight flight = this.flightRepo.findById(flightId).get();
        Plane plane = this.planeRepo.findById(planeId).get();
        flight.setPlane(plane);
        return this.flightRepo.save(flight);
    }


}
