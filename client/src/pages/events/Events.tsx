import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { getEventList } from '../../api/event.service';
import { AddEventBtn } from '../../components/addEventBtn/AddEventBtn';
import { EventsListBuild } from '../../components/eventsListBuild/EventsListBuild';
import { EventDto } from '../../interfaces/Event.dto';

interface EventState {
  events: EventDto[];
  pageSettings: {
    isLast: true | false
    nextPage: string | null
  };
}

export class Events extends Component<EventDto, EventState> {
  constructor(props: any) {
    super(props);
    this.state = {
      events: [
        {
          description: '',
          endDate: '',
          gallery: {
            id: undefined,
            imageUrls: ['https://via.placeholder.com/250'],
            isDeleted: undefined
          },
          id: 0,
          location: '',
          participants: [],
          startDate: '',
          topic: ''
        }
      ],
      pageSettings: {
        isLast: false,
        nextPage: ''
      }
    };
    this.fetchEvents.bind(this);
  }

  public componentDidMount() {
    this.fetchEvents();
  }

  public async fetchEvents(): Promise<void> {
    const response = await getEventList(this.state.pageSettings.nextPage);
    this.setState({
      events: [...response.content],
      pageSettings: {
        isLast: response.last,
        nextPage: !!sessionStorage.getItem('nextpage')
          ? sessionStorage.getItem('nextpage')
          : '/event/all'
      }
    });
  }

  public render() {
    return (
      <div className='container-fluid'>
        <h1 className='text-center'>Event List</h1>
        <AddEventBtn />
        <div className='row'>
          <div className='col'>
            <InfiniteScroll
              style={{
                MozColumnGap: '0.5em',
                MozColumnWidth: '15em',
                WebkitColumnGap: '0.5em',
                WebkitColumnWidth: '15em'
              }}
              dataLength={this.state.events.length} // This is important field to render the next data
              next={this.fetchEvents}
              hasMore={!this.state.pageSettings.isLast}
              loader={<h4>Loading...</h4>}
              endMessage={
                <p style={{ textAlign: 'center' }}>
                  <b>Yay! You have seen it all</b>
                </p>
              }
            >
              {this.state.events.map((event, index) => (
                <EventsListBuild {...event} key={index} />
              ))}
            </InfiniteScroll>
          </div>
        </div>
      </div>
    );
  }
}
