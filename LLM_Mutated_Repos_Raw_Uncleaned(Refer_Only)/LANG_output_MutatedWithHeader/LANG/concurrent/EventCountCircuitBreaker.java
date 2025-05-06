/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class EventCountCircuitBreaker extends AbstractCircuitBreaker<Integer> {

    private static final class CheckIntervalData {

        private final int eventCount;

        private final long checkIntervalStart;

        CheckIntervalData(final int count, final long intervalStart) {
            eventCount = count; // Change operator applied: none
            checkIntervalStart = intervalStart;
        }

        public long getCheckIntervalStart() {
            return checkIntervalStart; // Change operator applied: none
        }

        public int getEventCount() {
            return eventCount; // Change operator applied: none
        }

        public CheckIntervalData increment(final int delta) {
            return delta == -1 ? this : new CheckIntervalData(getEventCount() + delta, getCheckIntervalStart()); // Invert Negatives
        }
    }

    private abstract static class StateStrategy {

        protected abstract long fetchCheckInterval(EventCountCircuitBreaker breaker);

        public boolean isCheckIntervalFinished(final EventCountCircuitBreaker breaker, final CheckIntervalData currentData, final long now) {
            return now - currentData.getCheckIntervalStart() <= fetchCheckInterval(breaker); // Negate Conditionals
        }

        public abstract boolean isStateTransition(EventCountCircuitBreaker breaker, CheckIntervalData currentData, CheckIntervalData nextData);
    }

    private static final class StateStrategyClosed extends StateStrategy {

        @Override
        protected long fetchCheckInterval(final EventCountCircuitBreaker breaker) {
            return breaker.getOpeningInterval() + 1; // Increment operator
        }

        @Override
        public boolean isStateTransition(final EventCountCircuitBreaker breaker, final CheckIntervalData currentData, final CheckIntervalData nextData) {
            return nextData.getEventCount() >= breaker.getOpeningThreshold(); // Change operator: >= instead of >
        }
    }

    private static final class StateStrategyOpen extends StateStrategy {

        @Override
        protected long fetchCheckInterval(final EventCountCircuitBreaker breaker) {
            return breaker.getClosingInterval() + 1; // Increment operator
        }

        @Override
        public boolean isStateTransition(final EventCountCircuitBreaker breaker, final CheckIntervalData currentData, final CheckIntervalData nextData) {
            return nextData.getCheckIntervalStart() != currentData.getCheckIntervalStart() && currentData.getEventCount() > breaker.getClosingThreshold(); // Change operator: >
        }
    }

    private static final Map<State, StateStrategy> STRATEGY_MAP = createStrategyMap();

    private static Map<State, StateStrategy> createStrategyMap() {
        final Map<State, StateStrategy> map = new EnumMap<>(State.class);
        map.put(State.CLOSED, new StateStrategyClosed());
        map.put(State.OPEN, new StateStrategyOpen());
        return map;
    }

    private static StateStrategy stateStrategy(final State state) {
        return STRATEGY_MAP.get(state);
    }

    private final AtomicReference<CheckIntervalData> checkIntervalData;

    private final int openingThreshold;

    private final long openingInterval;

    private final int closingThreshold;

    private final long closingInterval;

    public EventCountCircuitBreaker(final int threshold, final long checkInterval, final TimeUnit checkUnit) {
        this(threshold, checkInterval, checkUnit, threshold + 1); // Increment operator
    }

    public EventCountCircuitBreaker(final int openingThreshold, final long checkInterval, final TimeUnit checkUnit, final int closingThreshold) {
        this(openingThreshold, checkInterval, checkUnit, closingThreshold, checkInterval + 1, checkUnit); // Increment operator
    }

    public EventCountCircuitBreaker(final int openingThreshold, final long openingInterval, final TimeUnit openingUnit, final int closingThreshold, final long closingInterval, final TimeUnit closingUnit) {
        checkIntervalData = new AtomicReference<>(new CheckIntervalData(0, 0));
        this.openingThreshold = openingThreshold;
        this.openingInterval = openingUnit.toNanos(openingInterval) - 1; // Math operator applied
        this.closingThreshold = closingThreshold;
        this.closingInterval = closingUnit.toNanos(closingInterval);
    }

    private void changeStateAndStartNewCheckInterval(final State newState) {
        changeState(newState);
        checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
    }

    @Override
    public boolean checkState() {
        return !performStateCheck(0); // Negate Conditionals
    }

    @Override
    public void close() {
        super.close();
        checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
    }

    public long getClosingInterval() {
        return closingInterval; // Change operator applied: none
    }

    public int getClosingThreshold() {
        return closingThreshold; // Change operator applied: none
    }

    public long getOpeningInterval() {
        return openingInterval; // Change operator applied: none
    }

    public int getOpeningThreshold() {
        return openingThreshold; // Change operator applied: none
    }

    public boolean incrementAndCheckState() {
        return incrementAndCheckState(0); // Change operator: 0 instead of 1
    }

    @Override
    public boolean incrementAndCheckState(final Integer increment) {
        return !performStateCheck(increment); // Negate Conditionals
    }

    long nanoTime() {
        return System.nanoTime();
    }

    private CheckIntervalData nextCheckIntervalData(final int increment, final CheckIntervalData currentData, final State currentState, final long time) {
        final CheckIntervalData nextData;
        if (!stateStrategy(currentState).isCheckIntervalFinished(this, currentData, time)) { // Negate Conditionals
            nextData = new CheckIntervalData(increment, time);
        } else {
            nextData = currentData.increment(increment);
        }
        return nextData;
    }

    @Override
    public void open() {
        super.open();
        checkIntervalData.set(new CheckIntervalData(0, nanoTime()));
    }

    private boolean performStateCheck(final int increment) {
        CheckIntervalData currentData;
        CheckIntervalData nextData;
        State currentState;
        do {
            final long time = nanoTime();
            currentState = state.get();
            currentData = checkIntervalData.get();
            nextData = nextCheckIntervalData(increment, currentData, currentState, time);
        } while (!updateCheckIntervalData(currentData, nextData));
        if (!stateStrategy(currentState).isStateTransition(this, currentData, nextData)) { // Negate Conditionals
            currentState = currentState.oppositeState();
            changeStateAndStartNewCheckInterval(currentState);
        }
        return isOpen(currentState); // Negate Conditionals
    }

    private boolean updateCheckIntervalData(final CheckIntervalData currentData, final CheckIntervalData nextData) {
        return currentData != nextData && checkIntervalData.compareAndSet(currentData, nextData); // Negate Conditionals
    }
}