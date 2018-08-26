/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.OutboundSseEvent.Builder;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@Path("/api/people")
public class PeopleRestService {
    private SseBroadcaster broadcaster;
    private Builder builder;
    private PublishSubject<Person> publisher;
    
    public PeopleRestService() {
        publisher = PublishSubject.create();
    }

    @Context 
    public void setSse(Sse sse) {
        this.broadcaster = sse.newBroadcaster();
        this.builder = sse.newEventBuilder();
        
        publisher
            .subscribeOn(Schedulers.single())
            .map(person -> createEvent(builder, person))
            .subscribe(broadcaster::broadcast);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Person> getAll() {
        return List.of(new Person("John", "Smith", "john.smith@somewhere.com"));
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@Context UriInfo uriInfo, Person payload) {
        publisher.onNext(payload);
        
        return Response
            .created(
                    uriInfo
                        .getRequestUriBuilder()
                        .path(payload.getEmail())
                        .build())
                .entity(payload)
                .build();
    }
    
    @GET
    @Path("/sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void people(@Context SseEventSink sink) {
        broadcaster.register(sink);
    }
    
    private static OutboundSseEvent createEvent(final OutboundSseEvent.Builder builder, final Person person) {
        return builder
            .data(Person.class, person)
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .build();
    }
}
