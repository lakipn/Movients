import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITorrent } from 'app/shared/model/torrent.model';

@Component({
    selector: 'jhi-torrent-detail',
    templateUrl: './torrent-detail.component.html'
})
export class TorrentDetailComponent implements OnInit {
    torrent: ITorrent;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ torrent }) => {
            this.torrent = torrent;
        });
    }

    previousState() {
        window.history.back();
    }
}
