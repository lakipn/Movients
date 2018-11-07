/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MovientsTestModule } from '../../../test.module';
import { TorrentUpdateComponent } from 'app/entities/torrent/torrent-update.component';
import { TorrentService } from 'app/entities/torrent/torrent.service';
import { Torrent } from 'app/shared/model/torrent.model';

describe('Component Tests', () => {
    describe('Torrent Management Update Component', () => {
        let comp: TorrentUpdateComponent;
        let fixture: ComponentFixture<TorrentUpdateComponent>;
        let service: TorrentService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [TorrentUpdateComponent]
            })
                .overrideTemplate(TorrentUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TorrentUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TorrentService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Torrent(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.torrent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Torrent();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.torrent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
